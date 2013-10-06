package eu.epitech.mediaplayer;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.sql.Timestamp;

import net.minecraft.Unzip;
import net.minecraft.Util;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.NativeLibrary;


public class VlcLib {

	private static boolean alreadyInitialized = false;

	private static String getOS() {
		if (RuntimeUtil.isWindows())
			return "win";
		else if (RuntimeUtil.isMac())
			return "mac";
		else if (RuntimeUtil.isNix())
			return ("nix");
		return "unknown";
	}

	public static boolean initVlcLib() throws Exception {
		if (alreadyInitialized)
			return true;
		System.out.println("Initializing libvlc...");
		NativeDiscovery discovery = new NativeDiscovery();
		System.out.println("Searching libvlc on your machine...");
		if (discovery.discover() == true){
			System.out.println("Libvlc found on default installation path.");
			alreadyInitialized = true;
			return true;
		}
		System.out.println("Libvlc NOT found on default installation path.");
		@SuppressWarnings({ "unchecked", "rawtypes" })
		String path = (String)AccessController.doPrivileged(new PrivilegedExceptionAction() 
		{
			public Object run() throws Exception 
			{
				return Util.getWorkingDirectory() + File.separator + "lib" + File.separator;
			}
		});
		File libDir = new File(path);
		if (!libDir.exists())
			libDir.mkdirs(); //create %appdata%/.vose/lib

		String os = getOS();
		if (os == "unknown") {
			throw new Exception("OS not supported for media player.");
		}
		if (os.equals("nix")) {
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "/usr/lib/");
		}else {
			File libvlc = new File(path + "tmp.zip");
			File tmpdir = new File(path + "libvlc");
			if (!libvlc.exists() && !tmpdir.exists()) {
				//InputStream libvlclink = VlcLib.class.getResourceAsStream("/eu/epitech/mediaplayer/libs/libvlc_" + os + ".zip");
				System.out.println("Downloading vlclib...");
				InputStream libvlclink = new URL("http://jpo-virtuelle-epitech.eu/VOSE/libvlc/libvlc_" + os + ".zip").openStream();
				Files.copy(libvlclink, libvlc.getAbsoluteFile().toPath());
				Unzip.unzip(libvlc, tmpdir);
				libvlc.delete();
			}
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), tmpdir.getAbsolutePath());
		}
		alreadyInitialized = true;
		return true;
	}
}
