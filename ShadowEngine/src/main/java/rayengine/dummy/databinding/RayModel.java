package rayengine.dummy.databinding;

import java.util.ArrayList;
import java.util.List;

import rayengine.algorithms.IShadowAlgorithm;

public class RayModel {
	private static int inclination;
	private static double inclinationFactor;
	private static int pixelSize = 8;
	private static int azimuth;
	private static int altitude;
	private static IShadowAlgorithm rayAlgorithm;
	private static final List<IAzimuthListener> azimuthListeners = new ArrayList<>();
	private static final List<IAltitudeListener> altitudeListeners = new ArrayList<>();
	private static final List<IGenericUpdateListener> genericUpdateListeners = new ArrayList<>();
	
	
	public static void addAngleListener(IAngleListener listener) {
		azimuthListeners.add(listener);
		altitudeListeners.add(listener);
	}
	
	public static void addAzimuthListener(IAzimuthListener listener) {
		azimuthListeners.add(listener);
	}
	
	public static void addAltitudeListener(IAltitudeListener listener) {
		altitudeListeners.add(listener);
	}
	
	public static void addGenericUpdateListener(IGenericUpdateListener listener) {
		genericUpdateListeners.add(listener);
	}
	
	public static void setAzimuth(int azimuth) {
		RayModel.azimuth = azimuth;
		azimuthListeners.forEach(listener -> listener.updateAzimuth(azimuth));
	}
	
	public static void setAltitude(int altitude) {
		RayModel.altitude = altitude;
		altitudeListeners.forEach(listener -> listener.updateAltitude(altitude));
	}

	public static void setPixelSize(int newPixelSize) {
		pixelSize = newPixelSize;
		genericUpdateListeners.forEach(IGenericUpdateListener::update);
	}

	public static void setInclination(int inclination) {
		RayModel.inclination = inclination;
		inclinationFactor = Math.sin(inclination*Math.PI/180);
		genericUpdateListeners.forEach(IGenericUpdateListener::update);
	}
	
	public static void setRayAlgorithm(IShadowAlgorithm rayAlgorithm) {
		RayModel.rayAlgorithm = rayAlgorithm;
		genericUpdateListeners.forEach(IGenericUpdateListener::update);
	}
	
	public static void genericUpdate() {
		genericUpdateListeners.forEach(IGenericUpdateListener::update);
	}
	
	public static int getAzimuth() {
		return azimuth;
	}
	
	public static int getAltitude() {
		return altitude;
	}

	public static int getPixelSize() {
		return pixelSize;
	}

	public static int getInclination() {
		return inclination;
	}

	public static double getInclinationFactor() {
		return inclinationFactor;
	}
	
	public static IShadowAlgorithm getRayAlgorithm() {
		return rayAlgorithm;
	}
}
