package autoscanTest;

public class main {
    public static void main(String[] args) {

        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String msg = sdf.format(new Date());
        String path = "c:/temp/barcode.png";
        File file = new File(path);

        Code39Bean bean = new Code39Bean();
        // 精细度
        final int dpi = 150;
        // module宽度
        final double moduleWidth = UnitConv.in2mm(1.0f / dpi);

        // 配置对象
        bean.setModuleWidth(moduleWidth);
        //bean.setWideFactor(3);
        bean.doQuietZone(false);

        String format = "image/png";
        try {
            OutputStream ous = new FileOutputStream(file);
            // 输出到流
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format, dpi,
                    BufferedImage.TYPE_BYTE_BINARY, false, 0);

            // 生成条形码
            bean.generateBarcode(canvas, msg);

            // 结束绘制
            canvas.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

//        int latestNum = -1;
//        Format f1 = new DecimalFormat("000");
//        SimpleDateFormat sdf = new SimpleDateFormat("MMYY");
//        System.out.println(
//                String.format("CNS%s%s", sdf.format(new Date()), f1.format(latestNum)));

        /*String contents = "6943620593115";
        int width = 105, height = 50;
        int codeWidth = 3 + // start guard
                (7 * 6) + // left bars
                5 + // middle guard
                (7 * 6) + // right bars
                3; // end guard
        codeWidth = Math.max(codeWidth, width);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
                    BarcodeFormat.CODE_39, codeWidth, height, null);

            Path path1 = new File("C:\\temp\\img\\test.png").toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, "png",path1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String filePath = "c:/temp/1.png";
        BufferedImage image;
        try {
            image = ImageIO.read(new File(filePath));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(binaryBitmap, null);// 对图像进行解码
            System.out.println(result.getText());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }*/
        String str = "01,04,1200164320,02388138,224.27,20170430,72421672501935626208,3C4C,";
        String str1 = "01,04,1200164320,02388138,224.27,20170430,,3C4C,";
        System.out.println(str.split(",",0).length);
        System.out.println(str1.split(",", -1).length);
    }
}
