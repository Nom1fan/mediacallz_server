package mediacallz.com.server.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class EventReport implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -224696528176497072L;
	private EventType status;
	private String desc;
	private Object data;
	
	public EventReport(EventType status, String desc, Object data) {
		this.status = status;
		this.desc = desc;
		this.data = data;
	}

	public EventReport(EventType status, Object data) {
		this.status = status;
		this.data = data;
	}

	public EventReport(EventType status, String desc) {
		this.desc = desc;
		this.status = status;
	}

	public EventReport(EventType status) {
	    this.status = status;
    }

	@Override
	public String toString() {
		return "EventReport{" +
				"status=" + (status!=null ? status : "null") +
				", desc='" + (desc!=null ? desc : "null") + '\'' +
				", data=" + (data!=null ? data : "null") +
				'}';
	}
}
