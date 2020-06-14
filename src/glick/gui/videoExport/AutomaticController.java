package glick.gui.videoExport;

import java.awt.image.BufferedImage;
import java.io.IOException;

import glick.Launcher;
import glick.gui.Display;
import io.humble.video.Codec;
import io.humble.video.Encoder;
import io.humble.video.MediaPacket;
import io.humble.video.MediaPicture;
import io.humble.video.Muxer;
import io.humble.video.MuxerFormat;
import io.humble.video.PixelFormat;
import io.humble.video.Rational;
import io.humble.video.awt.MediaPictureConverter;
import io.humble.video.awt.MediaPictureConverterFactory;

public class AutomaticController extends Thread {
	Display display;

	Timeline scale;
	Timeline center;
	Timeline limit;
	Timeline colorOffset;
	double FPS;
	double length; // length of video in seconds

	public AutomaticController(Display display) {
		this.display = display;

		scale = new Timeline();
		center = new Timeline();
		limit = new Timeline();
		colorOffset = new Timeline();
		FPS = 60;
		length = 1;
	}

	public void run() {
		display.waitCursor(true);
		Launcher launcher = display.getLauncher();
		launcher.setIsVideoExporting(true);
		display.progressBar.setMinimum(0);
		// add 30% for video exporting
		display.progressBar.setMaximum((int) (length * FPS * 1.3));
		display.progressBar.setValue(0);
		// display.progressBar.setMinimum(0);
		// display.progressBar.setMaximum((int) (FPS * length));

		BufferedImage[] images = new BufferedImage[(int) (FPS * length)];
		for (int frame = 0; frame < length * FPS; frame++) {
			// launcher.setScale(Math.pow(2, scale.getValue(frame)));
			// TODO: implement translation launcher.setCenter(center.getValue(frame));
			launcher.setLimit((int) limit.getValue(frame));
			display.setColorOffset(colorOffset.getValue(frame));

			display.isUpdaterWorking = true;
			launcher.calculate(launcher);
			display.repaint();
			display.isUpdaterWorking = false;
			display.progressBar.setValue((int) (frame * 0.7));
			display.updateUI();

			// convert buffered image to 3_BYTE_BGR and copy into images array
			BufferedImage buffImag = new BufferedImage(display.getWidth(), display.getHeight(),
					BufferedImage.TYPE_3BYTE_BGR);
			buffImag.getGraphics().drawImage(display.getBuffImage(), 0, 0, null);
			images[frame] = buffImag;
		}

		Muxer muxer = Muxer.make(display.getUserSaveFile().getAbsolutePath() + ".mp4", null, "MP4");

		MuxerFormat format = muxer.getFormat();
		Encoder encoder = Encoder.make(Codec.findEncodingCodec(format.getDefaultVideoCodecId()));

		encoder.setWidth(display.getWidth());
		encoder.setHeight(display.getHeight());
		PixelFormat.Type pixelFormat = PixelFormat.Type.PIX_FMT_YUV420P;
		encoder.setPixelFormat(pixelFormat);
		Rational timeBase = Rational.make(1, (int) FPS); // time per frame
		encoder.setTimeBase(timeBase);

		/**
		 * An annoynace of some formats is that they need global (rather than
		 * per-stream) headers, and in that case you have to tell the encoder. And since
		 * Encoders are decoupled from Muxers, there is no easy way to know this beyond
		 */
		if (format.getFlag(MuxerFormat.Flag.GLOBAL_HEADER))
			encoder.setFlag(Encoder.Flag.FLAG_GLOBAL_HEADER, true);

		encoder.open(null, null);

		muxer.addNewStream(encoder);

		try {
			muxer.open(null, null);
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// convert buffered image format from rgb to YCrCb 3BYTE_BGR for the video
		MediaPictureConverter converter = null;
		MediaPicture finalPicture = MediaPicture.make(encoder.getWidth(), encoder.getHeight(), pixelFormat);
		finalPicture.setTimeBase(timeBase);

		MediaPacket packet = MediaPacket.make();
		for (int i = 0; i < images.length; i++) {
			BufferedImage buffImag = images[i];

			// convert buffered image to media picture (finalPicture) for encoding
			if (converter == null) {
				converter = MediaPictureConverterFactory.createConverter(buffImag, finalPicture);
			}
			converter.toPicture(finalPicture, buffImag, i);

			do {
				encoder.encode(packet, finalPicture);
				if (packet.isComplete()) {
					muxer.write(packet, false);
				}
			} while (packet.isComplete());
		}

		/**
		 * Encoders, like decoders, sometimes cache pictures so it can do the right
		 * key-frame optimizations. So, they need to be flushed as well. As with the
		 * decoders, the convention is to pass in a null input until the output is not
		 * complete.
		 */
		do {
			encoder.encode(packet, null);
			if (packet.isComplete())
				muxer.write(packet, false);
		} while (packet.isComplete());

		muxer.close();

		System.out.println("auto done");
		launcher.setIsVideoExporting(false);
		display.progressBar.setValue(display.progressBar.getMaximum());
		display.waitCursor(false);
	}

	public void setLength(double length) {
		this.length = length;
	}

	public Timeline getScaleTimeline() {
		return scale;
	}

	public Timeline getCenterTimeline() {
		return center;
	}

	public Timeline getLimitTimeline() {
		return limit;
	}

	public Timeline getColorOffsetTimeline() {
		return colorOffset;
	}

}
