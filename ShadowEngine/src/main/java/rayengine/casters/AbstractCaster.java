package rayengine.casters;

import static rayengine.dummy.MathUtil.RADIANTS_PER_DEGREE;

import java.awt.Point;
import java.io.File;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import rayengine.dummy.databinding.RayModel;

public abstract class AbstractCaster<T extends AbstractSection> {

	private static final String BASE_PATH = new File("").getAbsolutePath();
	protected int leftBorderCoordinate;
	protected int bottomBorderCoordinate;
	protected int width;
	protected int height;
	protected int displayedWidth;
	protected int displayedHeight;
	protected Image image;
	protected Bounds[] leftToRightBounds;
	protected Bounds[] frontToBackBounds;
	protected T[] sections;
	
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
	
	private Bounds[] getBoundsFromAlpha(byte[] alphaData, int startingIndex){
		Bounds[] leftToRightBounds = new Bounds[height];
		for(int row = 0; row < height; row++) {
			leftToRightBounds[row] = new Bounds(alphaData, startingIndex+row*width, width);
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
			AbstractSection section = sections[row];
			int leftProjectedEllipsisBorder = pixelSize*leftToRightBounds[row].getLowerBound() + (int) Math.round(distanceFromBottom*relativeProjectionShiftX*pixelSize);
			int bottomProjectedEllipsisBorder = (int) Math.round((bottomOffsetY - frontToBackBounds[row].getLowerBound())*pixelSize*RayModel.getInclinationFactor()) + (int) Math.round(distanceFromBottom*relativeProjectionShiftY*pixelSize);
			section.setBorderCoordinates(leftProjectedEllipsisBorder, bottomProjectedEllipsisBorder);
		}
	}
	
	public void drawFillSections(GC gc) {
		for(AbstractSection section : sections) {
			section.drawFill(gc, getBottomLeftCoordinate());
		}
	}
	
	public void drawOutlineSections(GC gc) {
		for(AbstractSection section : sections) {
			section.drawOutline(gc, getBottomLeftCoordinate());
		}
	}
	
	public void draw(GC gc) {
		gc.drawImage(image, 0, 0, width, height
				, leftBorderCoordinate, bottomBorderCoordinate - displayedHeight
				, displayedWidth, displayedHeight);
	}
	
	public int getLeftBorderCoordinate() {
		return leftBorderCoordinate;
	}
	
	public int getBottomBorderCoordinate() {
		return bottomBorderCoordinate;
	}
	
	public Point getBottomLeftCoordinate() {
		return new Point(leftBorderCoordinate, bottomBorderCoordinate);
	}
	
	public void setLeftBorderCoordinate(int leftBoundCoordinate) {
		this.leftBorderCoordinate = leftBoundCoordinate;
	}
	
	public void setBottomBorderCoordinate(int bottomBoundCoordinate) {
		this.bottomBorderCoordinate = bottomBoundCoordinate;
	}

	public T[] getSections() {
		return sections;
	}
	
	public int getSheetWidth() {
		return width;
	}

	public int getSheetHeight() {
		return height;
	}

	public Bounds[] getLeftToRightBounds() {
		return leftToRightBounds;
	}

	public Bounds[] getFrontToBackBounds() {
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
	
	private void calculateSections() {
		int pixelSize = RayModel.getPixelSize();
		initializeSections(height);
		
		for(int row = 0; row < height; row++) {
			int width = leftToRightBounds[row].getWidth()*pixelSize;
			int height = (int) Math.round(frontToBackBounds[row].getWidth()*pixelSize*RayModel.getInclinationFactor());
			sections[row] = createSection(width, height);
		}
	}
	
	protected abstract void initializeSections(int height);
	
	protected abstract T createSection(int width, int height);
}
