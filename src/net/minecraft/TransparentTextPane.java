package net.minecraft;

import javax.swing.JTextPane;

public class TransparentTextPane extends JTextPane {
	private static final long serialVersionUID = -9201251790081326201L;

	public TransparentTextPane() {
		setOpaque(false);
	}
	
	public void concat(String str) {
		setText(getText().concat(str));
	}
}
