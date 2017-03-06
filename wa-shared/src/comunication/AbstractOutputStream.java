package comunication;

import java.io.IOException;
import java.io.OutputStream;

import lejos.util.Delay;

public abstract class AbstractOutputStream {
	protected OutputStream stream;
	public AbstractOutputStream(OutputStream stream){
		this.stream = stream;
	}
	protected void write(int b) throws IOException {
		stream.write(b);
		stream.flush();
		Delay.msDelay(CommConst.GRACE);
	}

	protected void write(byte[] b) throws IOException {
		stream.write(b);
		stream.flush();
		Delay.msDelay(CommConst.GRACE);
	}

	public void close() throws IOException {
		stream.close();
	}
}
