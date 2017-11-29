package autoscan.Biz;

import autoscan.dao.SequenceDao;

import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class BarCodeBiz {

    @Autowired
    private SequenceDao sequenceDaoImp;

    public void generateBarCode(String msg, String path) throws IOException {

        File file = new File(path);

        Code39Bean bean = new Code39Bean();
        // 精细度
        final int dpi = 150;
        // module宽度
        final double moduleWidth = UnitConv.in2mm(1.0f / dpi);

        // 配置对象
        bean.setModuleWidth(moduleWidth);
        bean.setWideFactor(3);
        bean.doQuietZone(false);

        String format = "image/png";
        OutputStream ous = new FileOutputStream(file);
        // 输出到流
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format, dpi,
                BufferedImage.TYPE_BYTE_BINARY, false, 0);

        // 生成条形码
        bean.generateBarcode(canvas, msg);

        // 结束绘制
        canvas.finish();
    }

    public String generateCNSString() throws Exception {
        String uuid = UUID.randomUUID().toString();
        sequenceDaoImp.insertSequence(uuid);

        int latestNum = sequenceDaoImp.getSequenceRowIdByUUID(uuid);
        Format f1 = new DecimalFormat("000");
        SimpleDateFormat sdf = new SimpleDateFormat("MMYY");

        if (latestNum > 0) {
            return String.format("CNS%s%s", sdf.format(new Date()), f1.format(latestNum));
        }

        throw new Exception("unexcepted error when generate sequence");
    }
}
