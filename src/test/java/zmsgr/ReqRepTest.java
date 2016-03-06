package zmsgr;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReqRepTest {
	static final String SOCKET = "ipc://rrsample.ipc";
	static final String UTF_8 = "UTF-8";

	ReqSocket client = new ReqSocket();
	RepSocket server = new RepSocket();

	@Before
	public void setUp() throws Exception {
		new Thread() {
			public void run() {
				server.bind(SOCKET);
				try {
					byte[] request = server.recv();
					assertNotNull(request);
					System.out.println("Server: Recieved " + new String(request, UTF_8));

					String goodbye = "Goodbye!";
					assertTrue(server.send(goodbye.getBytes(UTF_8)));
					System.out.println("Server: Sent " + goodbye);
				} catch (UnsupportedEncodingException e) {
					fail(e.toString());
				}
			}
		}.start();
	}

	@After
	public void tearDown() throws Exception {
		client.close();
		server.close();
	}

	@Test
	public void test() {
		client.connect(SOCKET);
		try {
			String hello = "Hello!";
			assertTrue(client.send(hello.getBytes(UTF_8)));
			System.out.println("Client: Sent " + hello);

			byte[] reply = client.recv();
			assertNotNull(reply);
			System.out.println("Client: Recieved " + new String(reply, UTF_8));
		} catch (UnsupportedEncodingException e) {
			fail(e.toString());
		}
	}
}
