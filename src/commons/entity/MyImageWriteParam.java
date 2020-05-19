package commons.entity;

import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.util.Locale;

/**
 * @author RENZHEHAO
 * @version 1.0.0
 * @ClassName MyImageWriteParam.java
 * @Description 图片压缩
 * @createTime 2020年01月25日 17:58:00
 */
public class MyImageWriteParam extends JPEGImageWriteParam {
    public MyImageWriteParam() {
        super(Locale.getDefault());
    }

    @Override
    public void setCompressionQuality(float quality) {
        if (quality < 0.0F || quality > 1.0F) {
            throw new IllegalArgumentException("Quality out-of-bounds!");
        }
        this.compressionQuality = 256 - (quality * 256);
    }
}
