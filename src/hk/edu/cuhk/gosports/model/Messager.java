package hk.edu.cuhk.gosports.model;

import java.io.Serializable;

/**
 * Message communicated between service and activity
 * 
 * @author EdwardChou edwardchou_gmail_com
 * @date 2015-4-23 上午12:00:15
 * @version V1.0
 * 
 */
public class Messager implements Serializable {

	private static final long serialVersionUID = 1L;

	private int type = -1;
	private int status = -1;
	private Object object;

	public Messager(int type, int status, Object object) {
		this.type = type;
		this.status = status;
		this.object = object;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

}
