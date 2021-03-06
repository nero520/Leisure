package com.leisure.core.tools;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 图像压缩算法自动生成的高清图片工具
 * 
 * @author xiaoxiong
 * 
 */
public class ImageCompress extends Frame {
	private static final long serialVersionUID = 48L;

	public static void main(String[] args) {
		String fileName = "F:/af4496a4-15ae-47c9-8279-6e095ebfd539.png";
		String gui = "";
		if (args.length > 0)
			fileName = args[0];
		if (args.length > 1)
			gui = "gui";
		if (gui.equals("gui")) {
			new ImageCompress(fileName);
		} else {
			long c = System.currentTimeMillis();
			ImageCompress.ImageScale(fileName, fileName + "-s."
					+ getFileExt(fileName).toLowerCase(), 160, 160);
			System.out.println("elapse time:"
					+ (System.currentTimeMillis() - c) / 1000.0f + "s");
		}
	}

	private static final String version = "ImageCompress v1.0";

	public ImageCompress(String fileName) {
		super(version);
		file = fileName;
		createUI();
		loadImage(fileName);
		setVisible(true);
	}

	/**
	 * A Hashtable member variable holds the image processing operations, keyed
	 * by their names.
	 */

	private Panel mControlPanel;

	private BufferedImage mBufferedImage;

	private Label labelWidth = new Label("width:");
	private TextField textWidth = new TextField(7);

	private Label labelHeight = new Label("height:");
	private TextField textHeight = new TextField(7);

	private String file;

	/**
	 * createUI() creates the user controls and lays out the window. It also
	 * creates the event handlers (as inner classes) for the user controls.
	 */
	private void createUI() {
		setFont(new Font("Serif", Font.PLAIN, 12));
		setLayout(new BorderLayout());
		final Label statusLabel = new Label("Welcome to " + version + ".");
		textWidth.setText("160");
		textHeight.setText("160");
		Button loadButton = new Button("Load...");
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				FileDialog fd = new FileDialog(ImageCompress.this);
				fd.setVisible(true);
				if (fd.getFile() == null)
					return;
				String path = fd.getDirectory() + fd.getFile();
				file = path;
				loadImage(path);
			}
		});

		Button buttonResize = new Button("Resize");
		buttonResize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				resizeImage(file);

			}
		});
		mControlPanel = new Panel();
		mControlPanel.add(loadButton);
		mControlPanel.add(labelWidth);
		mControlPanel.add(textWidth);
		mControlPanel.add(labelHeight);
		mControlPanel.add(textHeight);
		mControlPanel.add(buttonResize);
		mControlPanel.add(statusLabel);
		add(mControlPanel, BorderLayout.SOUTH);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
	}

	private void resizeImage(String fileName) {
		try {
			Image image = javax.imageio.ImageIO.read(new File(file));
			int imageWidth = image.getWidth(null);
			int imageHeight = image.getHeight(null);

			float scale = getRatio(imageWidth, imageHeight, Integer
					.parseInt(textWidth.getText()), Integer.parseInt(textWidth
					.getText()));
			imageWidth = (int) (scale * imageWidth);
			imageHeight = (int) (scale * imageHeight);

			image = image.getScaledInstance(imageWidth, imageHeight,
					Image.SCALE_AREA_AVERAGING);
			mBufferedImage = new BufferedImage(imageWidth, imageHeight,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = mBufferedImage.createGraphics();

			g2.drawImage(image, 0, 0, imageWidth, imageHeight, Color.white,
					null);

			float[] kernelData2 = { -0.125f, -0.125f, -0.125f, -0.125f, 2,
					-0.125f, -0.125f, -0.125f, -0.125f };
			Kernel kernel = new Kernel(3, 3, kernelData2);
			ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
			mBufferedImage = cOp.filter(mBufferedImage, null);
			repaint();
		} catch (IOException e) {
		}
	}

	private void loadImage(String fileName) {
		
		Image image = Toolkit.getDefaultToolkit().getImage(fileName);
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(image, 0);
		try {
			mt.waitForID(0);
		} catch (InterruptedException ie) {
			return;
		}
		if (mt.isErrorID(0))
			return;
		mBufferedImage = new BufferedImage(image.getWidth(null), image
				.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = mBufferedImage.createGraphics();
		g2.drawImage(image, null, null);
		adjustToImageSize();
		center();
		validate();
		repaint();
		setTitle(version + ": " + fileName);
	}

	private void adjustToImageSize() {
		if (!isDisplayable())
			addNotify();
		Insets insets = getInsets();
		int imageWidth = mBufferedImage.getWidth();
		int imageHeight = mBufferedImage.getHeight();
		imageWidth = 600;
		imageHeight = 550;
		int w = imageWidth + insets.left + insets.right;
		int h = imageHeight + insets.top + insets.bottom;
		h += mControlPanel.getPreferredSize().height;
		setSize(w, h);
	}

	/**
	 * Center this window in the user's desktop.
	 */
	private void center() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension d = getSize();
		int x = (screen.width - d.width) / 2;
		int y = (screen.height - d.height) / 2;
		setLocation(x, y);
	}

	/**
	 * All paint() has to do is show the current image.
	 */
	public void paint(Graphics g) {
		if (mBufferedImage == null)
			return;
		Insets insets = getInsets();
		g.drawImage(mBufferedImage, insets.left, insets.top, null);
	}

	public static void ImageScale(String sourceImg, String targetImg,
			int width, int height) {
		try {
			Image image = javax.imageio.ImageIO.read(new File(sourceImg));
			int imageWidth = image.getWidth(null);
			int imageHeight = image.getHeight(null);
			float scale = getRatio(imageWidth, imageHeight, width, height);
			imageWidth = (int) (scale * imageWidth);
			imageHeight = (int) (scale * imageHeight);

			image = image.getScaledInstance(imageWidth, imageHeight,
					Image.SCALE_AREA_AVERAGING);
			BufferedImage mBufferedImage = new BufferedImage(imageWidth,
					imageHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = mBufferedImage.createGraphics();
			g2.drawImage(image, 0, 0, imageWidth, imageHeight, Color.white,
					null);
			g2.dispose();

			float[] kernelData2 = { -0.125f, -0.125f, -0.125f, -0.125f, 2,
					-0.125f, -0.125f, -0.125f, -0.125f };
			Kernel kernel = new Kernel(3, 3, kernelData2);
			ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
			mBufferedImage = cOp.filter(mBufferedImage, null);
			FileOutputStream out = new FileOutputStream(targetImg);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(mBufferedImage);
			out.close();
		} catch (FileNotFoundException fnf) {
		} catch (IOException ioe) {
		} finally {

		}
	}

	public static float getRatio(int width, int height, int maxWidth,
			int maxHeight) {
		float Ratio = 1.0f;
		float widthRatio;
		float heightRatio;
		widthRatio = (float) maxWidth / width;
		heightRatio = (float) maxHeight / height;
		if (widthRatio < 1.0 || heightRatio < 1.0) {
			Ratio = widthRatio <= heightRatio ? widthRatio : heightRatio;
		}
		return Ratio;
	}

	public static String getFileExt(String filePath) {
		String tmp = filePath.substring(filePath.lastIndexOf(".") + 1);
		return tmp.toUpperCase();
	}

	public static String getFileName(String filePath) {
		int pos = -1;
		int endPos = -1;
		if (!filePath.equals("")) {
			if (filePath.lastIndexOf("/") != -1) {
				pos = filePath.lastIndexOf("/") + 1;
			} else if (filePath.lastIndexOf("//") != -1) {
				pos = filePath.lastIndexOf("//") + 1;
			}
			if (pos == -1)
				pos = 0;
			filePath = filePath.substring(pos);
			endPos = filePath.lastIndexOf(".");
			if (endPos == -1) {
				return filePath;
			} else {
				return filePath.substring(0, endPos);
			}
		} else {
			return "";
		}
	}

	public static String getFileFullName(String filePath) {
		int pos = -1;
		if (!filePath.equals("")) {
			if (filePath.lastIndexOf("/") != -1) {
				pos = filePath.lastIndexOf("/") + 1;
			} else if (filePath.lastIndexOf("//") != -1) {
				pos = filePath.lastIndexOf("//") + 1;
			}
			if (pos == -1)
				pos = 0;
			return filePath.substring(pos);
		} else {
			return "";
		}
	}

	public static String getFilePath(String filePath) {
		int pos = -1;
		if (!filePath.equals("")) {
			if (filePath.lastIndexOf("/") != -1) {
				pos = filePath.lastIndexOf("/") + 1;
			} else if (filePath.lastIndexOf("//") != -1) {
				pos = filePath.lastIndexOf("//") + 1;
			}
			if (pos != -1) {
				return filePath.substring(0, pos);
			} else {
				return "";
			}
		} else {
			return "";
		}
	}
}