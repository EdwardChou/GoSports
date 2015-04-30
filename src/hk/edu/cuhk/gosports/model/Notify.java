package hk.edu.cuhk.gosports.model;

/**
 * 
 * interface to notify refresh result
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-21 PM10:50:40
 * @version V1.0
 * 
 */
public interface Notify {

	public void init();

	public void refresh(Object... params);

}
