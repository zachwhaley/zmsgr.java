package zmsgr;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class RepSocket {
	private ZContext mZMQCtx = new ZContext();
	private ZMQ.Socket mSocket = mZMQCtx.createSocket(ZMQ.REP);
	
	public void close() {
		mZMQCtx.destroy();
	}
	
	public void bind(String socket) {
		mSocket.bind(socket);
	}
	
	public boolean send(byte[] data) {
		return mSocket.send(data, 0);
	}
	
	public byte[] recv() {
		return mSocket.recv();
	}
}
