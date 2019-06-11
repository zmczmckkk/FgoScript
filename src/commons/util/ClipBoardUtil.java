package commons.util;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
public class ClipBoardUtil {
	 /**
     *1. 从剪切板获得文字。
     */
    public static String getSysClipboardText() {
        String ret = "";
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 获取剪切板中的内容
        Transferable clipTf = sysClip.getContents(null);

        if (clipTf != null) {
            // 检查内容是否是文本类型
            if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    ret = (String) clipTf
                            .getTransferData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }

    /**
     * 2.将字符串复制到剪切板。
     */
    public static void setSysClipboardText(String writeMe) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }

    /**
     *3. 从剪切板获得图片。
     */
    public static Image getImageFromClipboard() throws Exception {
        Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable cc = sysc.getContents(null);
        if (cc == null) {
            return null;
        } else if (cc.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            return (Image) cc.getTransferData(DataFlavor.imageFlavor);
        }
        return null;
        
    }

    /**
     * 4.复制图片到剪切板。
     */
    public static void setClipboardImage(final Image image) {
        Transferable trans = new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[] { DataFlavor.imageFlavor };
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return DataFlavor.imageFlavor.equals(flavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor)
                    throws UnsupportedFlavorException {
                if (isDataFlavorSupported(flavor)) {
                    return image;
                }
                throw new UnsupportedFlavorException(flavor);
            }

        };
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans,
                null);
    }

    /**
     * 5.通过流获取，可读取图文混合
     */
    public void getImageAndTextFromClipboard() throws Exception{
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable clipTf = sysClip.getContents(null);
        DataFlavor[] dataList = clipTf.getTransferDataFlavors();
        int wholeLength = 0;
        for (int i = 0; i < dataList.length; i++) {
            DataFlavor data = dataList[i];
            if (data.getSubType().equals("rtf")) {
                Reader reader = data.getReaderForText(clipTf);
                OutputStreamWriter osw = new OutputStreamWriter(
                        new FileOutputStream("d:\\test.rtf"));
                char[] c = new char[1024];
                int leng;
                while ((leng = reader.read(c)) != -1) {
                    osw.write(c, wholeLength, leng);
                }
                osw.flush();
                osw.close();
            }
        }
    }

	public static void main(String[] args) {
//		String path = "C:\\Users\\zmczmckkk\\OneDrive\\Code\\FgoScript\\src\\333.txt";
		try {
			InputStream is = new FileInputStream("C:\\Users\\zmczmckkk\\OneDrive\\Code\\FgoScript\\src\\NewFile.html");
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String str;
			while (true) {
				str = reader.readLine();
				if(str!=null) {
					if (str.contains("<") || str.contains(">")) {
						str = reduceStr(str);
					}
				} else {
					break;
				}
			}
			
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String reduceStr(String str) {
		String ss ="";
		int lLoc = str.indexOf("<");
		int rLoc = str.indexOf(">");
		if (lLoc < rLoc && lLoc != -1) {
			ss = str.substring(0, lLoc)+ str.substring(rLoc+1);
		}else if (lLoc > rLoc && rLoc != -1) {
			ss = str.substring(rLoc+1);
		}else if (lLoc!=-1 && rLoc==-1) {
			ss = str.substring(0, lLoc);
		}else if (lLoc==-1 && rLoc!=-1) {
			ss = str.substring(rLoc+1);
		}
		if (ss.contains("<") || ss.contains(">")) {
			ss = reduceStr(ss);
		}
		return ss;
	}
}
