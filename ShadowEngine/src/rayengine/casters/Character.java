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
	private int _leftBoundCoordinate;
	private int _bottomBoundCoordinate;
	private int _sheetWidth;
	private int _sheetHeight;
	private int _displayedWidth;
	private int _displayedHeight;
	private Image _image;
	private int[][] _frontBounds;
	private int[][] _rightBounds;
	private Ellipsis[] _sections;
	
	public Character(String relavitePath, Display display) {
		_image = new Image(display, BASE_PATH + File.separatorChar + relavitePath);
		ImageData imageData = _image.getImageData();
		_sheetWidth = imageData.width;
		_sheetHeight = imageData.height/4;
		_displayedWidth = _sheetWidth*RayModel.getPixelSize();
		_displayedHeight = _sheetHeight*RayModel.getPixelSize();
		calculateBounds(imageData);
		calculateSections();
	}

	private void calculateBounds(ImageData imageData) {
		_frontBounds = new int[_sheetHeight][2];
		_rightBounds = new int[_sheetHeight][2];
		byte[] alphaData = imageData.alphaData;
		for(int row = 0; row < _sheetHeight; row++) {
			int leftBoundFront = -1;
			int rightBoundFront = -1;
			int leftBoundRight = -1;
			int rightBoundRight = -1;
			for(int column = 0; column < _sheetWidth; column++) {
				if(alphaData[column + row*_sheetWidth] != 0) {
					rightBoundFront = column;
					if(leftBoundFront == -1) {
						leftBoundFront = column;
					}
				}
				if(alphaData[column + row*_sheetWidth + _sheetWidth*_sheetHeight] != 0) {
					rightBoundRight = column;
					if(leftBoundRight == -1) {
						leftBoundRight = column;
					}
				}
			}
			_frontBounds[row][0] = leftBoundFront;
			_frontBounds[row][1] = rightBoundFront;
			_rightBounds[row][0] = leftBoundRight;
			_rightBounds[row][1] = rightBoundRight;
		}
	}
	
	private void calculateSections() {
		int pixelSize = RayModel.getPixelSize();
		_sections = new Ellipsis[_sheetHeight];
		
		for(int row = 0; row < _sheetHeight; row++) {
			int axisX = (_frontBounds[row][1] - _frontBounds[row][0] + 1)*pixelSize;
			int axisY = (int) Math.round((_rightBounds[row][1] - _rightBounds[row][0] + 1)*pixelSize*RayModel.getInclinationFactor());
			_sections[row] = new Ellipsis(axisX, axisY);
		}
	}
	
	public void castSections(int altitude, int azimuth) {
		int pixelSize = RayModel.getPixelSize();
		double relativeProjectionShiftX = Math.tan(altitude*RADIANTS_PER_DEGREE)*Math.sin(azimuth*RADIANTS_PER_DEGREE);
		double relativeProjectionShiftY = -Math.tan(altitude*RADIANTS_PER_DEGREE)*Math.cos(azimuth*RADIANTS_PER_DEGREE)*RayModel.getInclinationFactor();
		int bottomOffsetY = _rightBounds[_rightBounds.length-1][0];
				
		for(int row = 0; row < _sheetHeight; row++) {
			int distanceFromBottom = _sheetHeight-row-1;
			Ellipsis section = _sections[row];
			int leftProjectedEllipsisBorder = pixelSize*_frontBounds[row][0] + (int) Math.round(distanceFromBottom*relativeProjectionShiftX*pixelSize);
			int bottomProjectedEllipsisBorder = (bottomOffsetY - _rightBounds[row][0])*pixelSize + (int) Math.round(distanceFromBottom*relativeProjectionShiftY*pixelSize);
			section.setBounds(leftProjectedEllipsisBorder, bottomProjectedEllipsisBorder);
		}
	}
	
	public void draw(GC gc) {
		gc.drawImage(_image, 0, 0, _sheetWidth, _sheetHeight
				, _leftBoundCoordinate, _bottomBoundCoordinate - _displayedHeight
				, _displayedWidth, _displayedHeight);
	}
	
	public int getLeftBoundCoordinate() {
		return _leftBoundCoordinate;
	}
	
	public int getBottomBoundCoordinate() {
		return _bottomBoundCoordinate;
	}
	
	public void setLeftBoundCoordinate(int leftBoundCoordinate) {
		_leftBoundCoordinate = leftBoundCoordinate;
	}
	
	public void setBottomBoundCoordinate(int bottomBoundCoordinate) {
		_bottomBoundCoordinate = bottomBoundCoordinate;
	}

	public Ellipsis[] getSections() {
		return _sections;
	}
	
	public int getSheetWidth() {
		return _sheetWidth;
	}

	public int getSheetHeight() {
		return _sheetHeight;
	}

	public int[][] getFrontBounds() {
		return _frontBounds;
	}

	public int[][] getRightBounds() {
		return _rightBounds;
	}
	
	public int getDisplayedWidth() {
		return _displayedWidth;
	}

	public int getDisplayedHeight() {
		return _displayedHeight;
	}
	
	public Image getImage() {
		return _image;
	}

}
