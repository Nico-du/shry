package net.chinanets.service.imp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.chinanets.service.IZipUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 魏 剑
 *  
 */
public class ZipUtils implements IZipUtils {
    private Log logger = LogFactory.getLog(ZipUtils.class);

    private File zipFile;

    private String unzipDir;

    public ZipUtils(File zipFile, String unzipDir) {
        this.zipFile = zipFile;
        this.unzipDir = unzipDir;
    }

    public String unzip() throws Exception {
        logger.info("zipFile path=" + zipFile.getPath());
        ZipFile zf = new ZipFile(zipFile);
        Enumeration enu = zf.entries();
        String result = "";

        while (enu.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) enu.nextElement();
            String name = entry.getName();

            //如果解压entry是目录,直接生成目录即可,不用写入,如果是文件,要讲文件写入
            String path = unzipDir + name;
            result = result + path + "<br/>";
            File file = new File(path);
            if (entry.isDirectory()) {
                file.mkdirs();
            } else {

//建议使用如下方式创建 流,和读取字节,不然会有乱码(当然要根据具体环境来定),具体原因请听

//下回分解,呵呵
                InputStream is = zf.getInputStream(entry);
                byte[] buf1 = new byte[1024];
                int len;

                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                OutputStream out = new FileOutputStream(file);
                while ((len = is.read(buf1)) > 0) {
                    String buf = new String(buf1, 0, len);

                    out.write(buf1, 0, len);
                }
            }
        }
        result ="文件解压成功,解压文件:<br/>" + result; 
        logger.info("----------------unzip msg = "+result);
        return result;
    }
}

