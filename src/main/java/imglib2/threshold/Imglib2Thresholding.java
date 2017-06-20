package imglib2.threshold;




import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.stats.ComputeMinMax;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;
import org.scijava.ui.viewer.DisplayViewer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This plugin command thresholds an image at value t
 * </p>
 */
@Plugin(type = Command.class, menuPath = "Plugins>Threshold")
public class Imglib2Thresholding<T extends RealType<T>> implements Command {
    //
    // Feel free to add more parameters here...
    //

	@Parameter 
	private Img<T> activeImg;
	
    /*
	@Parameter
	private UIService uiService;
	
      
    @Parameter
    private OpService opService; 
    
    public void run() {
        //this is just a first line of code
    	//final Img<T> image = (Img<T>)currentData.getImgPlus();
    	
    	System.out.println(inputImg);

    }
 */

    
    public void run() {
    	
    	//IterableInterval<T> ii = Views.iterable(activeImg);
    	
    	final ImgFactory< UnsignedByteType > factory;
		factory = new ArrayImgFactory<UnsignedByteType>();
		
		Img<UnsignedByteType> outputImg = factory.create(activeImg, new UnsignedByteType());
    	
    	Cursor<T> cursor = activeImg.cursor();
    	
    	T min = cursor.next().createVariable();
    	T max = cursor.next().createVariable();
    	cursor.reset();
    	
    	ComputeMinMax<T> cmm = new ComputeMinMax<T>(activeImg, min , max);
    	cmm.process();
    	System.out.println(min);
    	System.out.println(max);
    	
    	Double threshold = (min.getRealDouble() + max.getRealDouble())/2;
      
    	RandomAccess<T> inRA = activeImg.randomAccess();
    	
    	Cursor<UnsignedByteType> outcursor = outputImg.cursor();
    	while(outcursor.hasNext()) {
    		
    		
    		outcursor.fwd();
    		inRA.setPosition(outcursor);
    		
    		/*
    		if(inRA.get().getRealDouble()>=threshold) {
    			outcursor.get().set(255);
    			//cursor.get().setReal(255.0);
    		} else {
    			outcursor.get().set(0);
    			//cursor.get().setReal(0.0);
    		}
    		*/
    		
    		boolean contains = inRA.get().getRealDouble()>=threshold; 
    		outcursor.get().set(contains ? 255 : 0);
    	}
    	
    	ImageJFunctions.show(outputImg);
    	

    	
		
	}
    
    
    
    
    
    
    
    /**
     * This main function serves for development purposes.
     * It allows you to run the plugin immediately out of
     * your integrated development environment (IDE).
     *
     * @param args whatever, it's ignored
     * @throws Exception
     */
    public static void main(final String... args) throws Exception {
        // create the ImageJ application context with all available services
        final ImageJ ij = new ImageJ();
        ij.ui().showUI();

        // ask the user for a file to open
        
        
    }

	

}
