package hk.edu.cuhk.gosports.utils;

/**
 * geo info calculation
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-29 下午7:26:57
 * @version V1.0
 * 
 */
public class GeoUtil {

	/**
	 * calculate the circle range of given latitude, longitude and radius
	 * 
	 * @param latitude
	 * @param longitude
	 * @param radius
	 *            meters
	 * @return minLatitude,minLongitude,maxLatitude,maxLongitude
	 */
	public static double[] getAround(double latitude, double longitude,
			int radius) {

		double PI = 3.1415926535898;

		Double degree = (24901 * 1609) / 360.0;
		double raidusMile = radius;

		Double dpmLat = 1 / degree;
		Double radiusLat = dpmLat * raidusMile;
		Double minLat = latitude - radiusLat;
		Double maxLat = latitude + radiusLat;

		Double mpdLng = degree * Math.cos(latitude * (PI / 180));
		Double dpmLng = 1 / mpdLng;
		Double radiusLng = dpmLng * raidusMile;
		Double minLng = longitude - radiusLng;
		Double maxLng = longitude + radiusLng;
		return new double[] { minLat, minLng, maxLat, maxLng };
	}

}
