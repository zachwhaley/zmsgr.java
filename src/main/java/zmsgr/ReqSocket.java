package zmsgr;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ReqSocket {
	private ZContext mZMQCtx = new ZContext();
	private ZMQ.Socket mSocket = mZMQCtx.createSocket(ZMQ.REQ);
	private String mRequestSock;
	
	public void close() {
		mZMQCtx.destroy();
	}
	
	public void connect(String socket) {
		mRequestSock = socket;
		mSocket.connect(mRequestSock);
	}
	
	public boolean send(byte[] data) {
		return mSocket.send(data, 0);
	}
	
	public boolean resend(byte[] data) {
		mZMQCtx.destroySocket(mSocket);
		mSocket = mZMQCtx.createSocket(ZMQ.REQ);
		connect(mRequestSock);
		return send(data);
	}
	
	public byte[] recv() {
		return recv(-1);
	}
	
	public byte[] recv(long timeout) {
		ZMQ.PollItem items[] = { new ZMQ.PollItem(mSocket, ZMQ.Poller.POLLIN) };
		if (ZMQ.poll(items, timeout) == -1) {
			return null;
		}
		if (items[0].isReadable()) {
			return mSocket.recv();
		}
		return null;
	}
}
