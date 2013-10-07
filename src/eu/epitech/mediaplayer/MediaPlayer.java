package eu.epitech.mediaplayer;

import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

/**
 * Test demonstrating the {@link EmbeddedMediaPlayerComponent}.
 * <p>
 * Leaving aside the standard Swing initialisation code, there are only <em>two</em> lines of vlcj
 * code required to create a media player and play a video.
 */
public class MediaPlayer extends IPlayer {

    private EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private String mrl;
    private Frame topFrame;
    private JPanel playPanel;
    
    //new MediaPlayer().start(mrl);

    /**
     * Create a new test.
     */
    public MediaPlayer(String link, Frame topFrame, JPanel playPanel) {
    	try {
			VlcLib.initVlcLib();
			System.out.println("Libvlc initialized successfully!");
		} catch (Exception e) {
			System.err.println("Fail to initialize libvlc");
			e.printStackTrace();
			return ;
		}
    	this.topFrame = topFrame;
    	this.playPanel = playPanel;
    	this.mrl = link;
    	setLookAndFeel();
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        mediaPlayerComponent.getMediaPlayer().setEnableKeyInputHandling(false);
        mediaPlayerComponent.getMediaPlayer().setEnableMouseInputHandling(false);
        MouseHandler mouseHandler = new MouseHandler();
        
        mediaPlayerComponent.getVideoSurface().addMouseListener(mouseHandler);
        mediaPlayerComponent.getVideoSurface().addMouseMotionListener(mouseHandler);
        mediaPlayerComponent.getVideoSurface().addMouseWheelListener(mouseHandler);
    }
    
    public EmbeddedMediaPlayerComponent getComponent() {
    	return mediaPlayerComponent;
    }

    public void start() {
    	if (mediaPlayerComponent.getMediaPlayer().isPlaying()) {
    		playPanel.setVisible(true);
			mediaPlayerComponent.getMediaPlayer().pause();
		}else {
			playPanel.setVisible(false);
			if (mediaPlayerComponent.getMediaPlayer().isPlayable())
				mediaPlayerComponent.getMediaPlayer().start();
			else
				mediaPlayerComponent.getMediaPlayer().playMedia(mrl);
		}
    }
    
    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent event) {
            start();
        }

        @Override
        public void mouseDragged(MouseEvent event) {
        }

        @Override
        public void mouseEntered(MouseEvent event) {
        }

        @Override
        public void mouseExited(MouseEvent event) {
        	topFrame.requestFocusInWindow();
        }

        @Override
        public void mouseMoved(MouseEvent event) {
        }

        @Override
        public void mousePressed(MouseEvent event) {
        }

        @Override
        public void mouseReleased(MouseEvent event) {
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent event) {
        }
    }
}
