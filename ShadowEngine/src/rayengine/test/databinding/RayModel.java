package rayengine.test.databinding;

import java.util.ArrayList;
import java.util.List;

import rayengine.algorithms.IRayAlgorithm;

public class RayModel {
	private static int _inclination;
	private static double _inclinationFactor;
	private static int _pixelSize = 8;
	private static int _azimuth;
	private static int _altitude;
	private static IRayAlgorithm _rayAlgorithm;
	private static final List<IAzimuthListener> _azimuthListeners = new ArrayList<>();
	private static final List<IAltitudeListener> _altitudeListeners = new ArrayList<>();
	private static final List<IGenericUpdateListener> _genericUpdateListeners = new ArrayList<>();
	
	
	public static void addAngleListener(IAngleListener listener) {
		_azimuthListeners.add(listener);
		_altitudeListeners.add(listener);
	}
	
	public static void addAzimuthListener(IAzimuthListener listener) {
		_azimuthListeners.add(listener);
	}
	
	public static void addAltitudeListener(IAltitudeListener listener) {
		_altitudeListeners.add(listener);
	}
	
	public static void addGenericUpdateListener(IGenericUpdateListener listener) {
		_genericUpdateListeners.add(listener);
	}
	
	public static void setAzimuth(int azimuth) {
		_azimuth = azimuth;
		_azimuthListeners.forEach(listener -> listener.updateAzimuth(_azimuth));
	}
	
	public static void setAltitude(int altitude) {
		_altitude = altitude;
		_altitudeListeners.forEach(listener -> listener.updateAltitude(_altitude));
	}

	public static void setPixelSize(int newPixelSize) {
		_pixelSize = newPixelSize;
		_genericUpdateListeners.forEach(IGenericUpdateListener::update);
	}

	public static void setInclination(int inclination) {
		_inclination = inclination;
		_inclinationFactor = Math.sin(_inclination*Math.PI/180);
		_genericUpdateListeners.forEach(IGenericUpdateListener::update);
	}
	
	public static void setRayAlgorithm(IRayAlgorithm rayAlgorithm) {
		_rayAlgorithm = rayAlgorithm;
		_genericUpdateListeners.forEach(IGenericUpdateListener::update);
	}
	
	public static void genericUpdate() {
		_genericUpdateListeners.forEach(IGenericUpdateListener::update);
	}
	
	public static int getAzimuth() {
		return _azimuth;
	}
	
	public static int getAltitude() {
		return _altitude;
	}

	public static int getPixelSize() {
		return _pixelSize;
	}

	public static int getInclination() {
		return _inclination;
	}

	public static double getInclinationFactor() {
		return _inclinationFactor;
	}
	
	public static IRayAlgorithm getRayAlgorithm() {
		return _rayAlgorithm;
	}
}
