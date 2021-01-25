package rayengine.casters;

import static rayengine.test.MathUtil.RADIANTS_PER_DEGREE;

import java.io.File;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import rayengine.test.databinding.RayModel;

public class Character {

	private static final String BASE_PATH = new File("").getAbsolutePath();
	private int leftBoundCoordinate;
	private int bottomBoundCoordinate;
	private int sheetWidth;
	private int sheetHeight;
	private int displayedWidth;
	private int displayedHeight;
	private Image image;
	private CasterBounds[] frontBounds;
	private CasterBounds[] rightBounds;
	private Ellipsis[] sections;
	
	public Character(String relavitePath, Display display) {
		image = new Image(display, BASE_PATH + File.separatorChar + relavitePath);
		ImageData imageData = image.getImageData();
		sheetWidth = imageData.width;
		sheetHeight = imageData.height/4;
		displayedWidth = sheetWidth*RayModel.getPixelSize();
		displayedHeight = sheetHeight*RayModel.getPixelSize();
		calculateBounds(imageData);
		calculateSections();
	}

	private void calculateBounds(ImageData imageData) {
		byte[] alphaData = imageData.alphaData;
		frontBounds = getBoundsFromAlpha(alphaData, 0);
		rightBounds = getBoundsFromAlpha(alphaData, sheetWidth*sheetHeight);
	}
	
	private CasterBounds[] getBoundsFromAlpha(byte[] alphaData, int startingIndex){
		CasterBounds[] leftToRightBounds = new CasterBounds[sheetHeight];
		for(int row = 0; row < sheetHeight; row++) {
			leftToRightBounds[row] = new CasterBounds(alphaData, startingIndex+row*sheetWidth, sheetWidth);
		}
		return leftToRightBounds;
	}
	
	private void calculateSections() {
		int pixelSize = RayModel.getPixelSize();
		sections = new Ellipsis[sheetHeight];
		
		for(int row = 0; row < sheetHeight; row++) {
			int axisX = frontBounds[row].getWidth()*pixelSize;
			int axisY = (int) Math.round(rightBounds[row].getWidth()*pixelSize*RayModel.getInclinationFactor());
			sections[row] = new Ellipsis(axisX, axisY);
		}
	}
	
	public void castSections(int altitude, int azimuth) {
		int pixelSize = RayModel.getPixelSize();
		double relativeProjectionShiftX = Math.tan(altitude*RADIANTS_PER_DEGREE)*Math.sin(azimuth*RADIANTS_PER_DEGREE);
		double relativeProjectionShiftY = -Math.tan(altitude*RADIANTS_PER_DEGREE)*Math.cos(azimuth*RADIANTS_PER_DEGREE)*RayModel.getInclinationFactor();
		int bottomOffsetY = rightBounds[rightBounds.length-1].getLowerBound();
				
		for(int row = 0; row < sheetHeight; row++) {
			int distanceFromBottom = sheetHeight-row-1;
			Ellipsis section = sections[row];
			int leftProjectedEllipsisBorder = pixelSize*frontBounds[row].getLowerBound() + (int) Math.round(distanceFromBottom*relativeProjectionShiftX*pixelSize);
			int bottomProjectedEllipsisBorder = (bottomOffsetY - rightBounds[row].getLowerBound())*pixelSize + (int) Math.round(distanceFromBottom*relativeProjectionShiftY*pixelSize);
			section.setBounds(leftProjectedEllipsisBorder, bottomProjectedEllipsisBorder);
		}
	}
	
	public void draw(GC gc) {
		gc.drawImage(image, 0, 0, sheetWidth, sheetHeight
				, leftBoundCoordinate, bottomBoundCoordinate - displayedHeight
				, displayedWidth, displayedHeight);
	}
	
	public int getLeftBoundCoordinate() {
		return leftBoundCoordinate;
	}
	
	public int getBottomBoundCoordinate() {
		return bottomBoundCoordinate;
	}
	
	public void setLeftBoundCoordinate(int leftBoundCoordinate) {
		this.leftBoundCoordinate = leftBoundCoordinate;
	}
	
	public void setBottomBoundCoordinate(int bottomBoundCoordinate) {
		this.bottomBoundCoordinate = bottomBoundCoordinate;
	}

	public Ellipsis[] getSections() {
		return sections;
	}
	
	public int getSheetWidth() {
		return sheetWidth;
	}

	public int getSheetHeight() {
		return sheetHeight;
	}

	public CasterBounds[] getFrontBounds() {
		return frontBounds;
	}

	public CasterBounds[] getRightBounds() {
		return rightBounds;
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
}
