package mediacallz.com.server.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class EventReport implements Serializable {

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
}
