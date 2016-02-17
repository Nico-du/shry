package barcode;

import java.io.*;
import javax.print.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;

public class test
{
 public static void test(String code)
 {
  try{
   DocFlavor flavor=DocFlavor.INPUT_STREAM.JPEG;
   
   //get a printer
   PrintService[] printers=PrintServiceLookup.lookupPrintServices( flavor, null);
   for( int i=0; i<printers.length; i++ ) System.out.println( printers[i].getName());
   PrintService printer=printers[0];
   
   //job
   DocPrintJob job=printer.createPrintJob();
 
   //document
   BufferedImage img=new BufferedImage( 400,300, BufferedImage.TYPE_USHORT_555_RGB );
   Graphics g=img.getGraphics();
   g.drawString(code, 100,100);
   ByteArrayOutputStream outstream=new ByteArrayOutputStream();
   ImageIO.write( img, "jpg", outstream);
   byte[] buf=outstream.toByteArray();
   InputStream stream=new ByteArrayInputStream(buf);
   Doc doc=new SimpleDoc(stream,flavor,null);
   
   //print
   job.print(doc,null);
  }
  catch(Exception e)
  {
   e.printStackTrace();
  }
 }
     //Main method
    public static void main(String[] args)
    {
     test("12345");
    }
}