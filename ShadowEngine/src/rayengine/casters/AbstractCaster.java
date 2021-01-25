package rayengine.casters;

import static rayengine.test.MathUtil.RADIANTS_PER_DEGREE;

import java.io.File;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import rayengine.test.databinding.RayModel;

public abstract class AbstractCaster {

	private static final String BASE_PATH = new File("").getAbsolutePath();
	protected int leftBorderCoordinate;
	protected int bottomBorderCoordinate;
	protected int width;
	protected int height;
	protected int displayedWidth;
	protected int displayedHeight;
	protected Image image;
	protected CasterBounds[] leftToRightBounds;
	protected CasterBounds[] frontToBackBounds;
	protected Ellipsis[] sections;
	
	public AbstractCaster(String relavitePath, Display display) {
		image = new Image(display, BASE_PATH + File.separatorChar + relavitePath);
		ImageData spriteSheet = image.getImageData();
		width = spriteSheet.width;
		height = spriteSheet.height/4;
		displayedWidth = width*RayModel.getPixelSize();
		displayedHeight = height*RayModel.getPixelSize();
		calculateBounds(spriteSheet);
		calculateSections();
	}

	private void calculateBounds(ImageData imageData) {
		byte[] alphaData = imageData.alphaData;
		leftToRightBounds = getBoundsFromAlpha(alphaData, 0);
		frontToBackBounds = getBoundsFromAlpha(alphaData, width*height);
	}
	
	private CasterBounds[] getBoundsFromAlpha(byte[] alphaData, int startingIndex){
		CasterBounds[] leftToRightBounds = new CasterBounds[height];
		for(int row = 0; row < height; row++) {
			leftToRightBounds[row] = new CasterBounds(alphaData, startingIndex+row*width, width);
		}
		return leftToRightBounds;
	}
	
	public void castSections(int altitude, int azimuth) {
		int pixelSize = RayModel.getPixelSize();
		double relativeProjectionShiftX = Math.tan(altitude*RADIANTS_PER_DEGREE)*Math.sin(azimuth*RADIANTS_PER_DEGREE);
		double relativeProjectionShiftY = -Math.tan(altitude*RADIANTS_PER_DEGREE)*Math.cos(azimuth*RADIANTS_PER_DEGREE)*RayModel.getInclinationFactor();
		int bottomOffsetY = frontToBackBounds[frontToBackBounds.length-1].getLowerBound();
				
		for(int row = 0; row < height; row++) {
			int distanceFromBottom = height-row-1;
			Ellipsis section = sections[row];
			int leftProjectedEllipsisBorder = pixelSize*leftToRightBounds[row].getLowerBound() + (int) Math.round(distanceFromBottom*relativeProjectionShiftX*pixelSize);
			int bottomProjectedEllipsisBorder = (bottomOffsetY - frontToBackBounds[row].getLowerBound())*pixelSize + (int) Math.round(distanceFromBottom*relativeProjectionShiftY*pixelSize);
			section.setBorderCoordinates(leftProjectedEllipsisBorder, bottomProjectedEllipsisBorder);
		}
	}
	
	public void draw(GC gc) {
		gc.drawImage(image, 0, 0, width, height
				, leftBorderCoordinate, bottomBorderCoordinate - displayedHeight
				, displayedWidth, displayedHeight);
	}public int getLeftBoundCoordinate() {
		return leftBorderCoordinate;
	}
	
	public int getBottomBorderCoordinate() {
		return bottomBorderCoordinate;
	}
	
	public void setLeftBorderCoordinate(int leftBoundCoordinate) {
		this.leftBorderCoordinate = leftBoundCoordinate;
	}
	
	public void setBottomBorderCoordinate(int bottomBoundCoordinate) {
		this.bottomBorderCoordinate = bottomBoundCoordinate;
	}

	public Ellipsis[] getSections() {
		return sections;
	}
	
	public int getSheetWidth() {
		return width;
	}

	public int getSheetHeight() {
		return height;
	}

	public CasterBounds[] getFrontBounds() {
		return leftToRightBounds;
	}

	public CasterBounds[] getRightBounds() {
		return frontToBackBounds;
	}
	
	public int getDisplayedWidth() {
		return displayedWidth;
	}

	public int getDisplayedHeight() {
		return displayedHeight;
	}
	
	public Image getImage() {
		return image;
	}
	
	protected abstract void calculateSections();
}
