package apm.common.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 
 * <p>@author Zaric
 * <p>@date 2013-7-10 上午10:26:11
 */
public class ImagesUtils {

    private static final String[] IMAGES_SUFFIXES = {
            "bmp", "jpg", "jpeg", "gif", "png", "tiff"
    };

    /**
     * 是否是图片附件
     *
     * @param filename
     * @return
     */
    public static boolean isImage(String filename) {
        if (filename == null || filename.trim().length() == 0) return false;
        return ArrayUtils.contains(IMAGES_SUFFIXES, FilenameUtils.getExtension(filename).toLowerCase());
    }

	/**
	 * bufferedImage转换成byte[]
	 * 
	 * @param bi
	 *            图片流
	 * @param format
	 *            转换图片格式（jpg,jpeg,png,gif）
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] imageToByte(BufferedImage bi, String format)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bi, format, baos);
		return baos.toByteArray();
	}
	
	/**
	 * 压缩图片，并打成流，按压缩比例压缩
	 * 
	 * @param sourceImgStream
	 *            压缩源图片流
	 * @param compression
	 *            设置压缩倍数，正比例压缩
	 * @param rotation
	 *            设置压缩的图片旋转角度
	 * @param snapHeightMax
	 *            缩放后最小高度
	 * @param snapWidthMax
	 *            缩放后最小宽度
	 * @return
	 * @throws Exception
	 */
	public static InputStream compressImage(InputStream sourceImgStream,
			double compression, int snapHeightMax, int snapWidthMax)
			throws Exception {
		InputStream result = null;
		try {
			Image tag = ImageIO.read(sourceImgStream);
			int widthSrc = tag.getWidth(null);
			int heightSrc = tag.getHeight(null);
			double scaleds = getScaling(widthSrc, heightSrc, compression,
					snapHeightMax, snapWidthMax);
			int w = (int) (widthSrc * scaleds);
			int h = (int) (heightSrc * scaleds);
			BufferedImage tagNew = new BufferedImage(w, h,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = tagNew.createGraphics();
			while (!(g2.drawImage(tag, 0, 0, w, h, null))) {
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception("压缩图片，并打成流，按压缩比例压缩", e);
				}
			}
			byte[] data = imageToByte(tagNew, "jpeg");
			result = new ByteArrayInputStream(data);
		} catch (Exception e) {
			throw new Exception("压缩图片异常", e);
		}
		return result;
	}

	/**
	 * 得到图像缩放比率
	 * 
	 * @param sourceWidth
	 *            源图宽
	 * @param sourceHeight
	 *            源图高
	 * @param scaled
	 *            变化比例值，要想把图片放大变小只需改变该值
	 * @param snapHeightMax
	 *            缩放后最小高度
	 * @param snapWidthMax
	 *            缩放后最小宽度
	 * @return double
	 */
	public static double getScaling(int sourceWidth, int sourceHeight, double scaled, int snapHeightMax, int snapWidthMax) {
		double widthScaling = ((double) snapWidthMax * (double) scaled) / (double) sourceWidth;
		double heightScaling = ((double) snapHeightMax * (double) scaled) / (double) sourceHeight;
		double scaling = (widthScaling < heightScaling) ? widthScaling : heightScaling;
		return scaling;
	}

}