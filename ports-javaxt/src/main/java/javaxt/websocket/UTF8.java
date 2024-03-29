package javaxt.websocket;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

import javaxt.websocket.Frame.CloseFrame;

//******************************************************************************
//**  UTF8
//******************************************************************************
/**
 * Used to encode/decode UTF-8 data.
 *
 ******************************************************************************/

public class UTF8 {

	private UTF8() {
	}

	private static CodingErrorAction codingErrorAction = CodingErrorAction.REPORT;

	// **************************************************************************
	// ** encode
	// **************************************************************************
	/**
	 * Converts a UTF-8 string to a byte array.
	 */
	public static byte[] encode(String s) {
		try {
			return s.getBytes("UTF8");
		} catch (java.io.UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	// **************************************************************************
	// ** decode
	// **************************************************************************
	/**
	 * Converts a byte array to a UTF-8 string.
	 */
	public static String decode(byte[] bytes) throws FrameException {
		return decode(ByteBuffer.wrap(bytes));
	}

	// **************************************************************************
	// ** decode
	// **************************************************************************
	/**
	 * Converts a ByteBuffer to a UTF-8 string.
	 */
	public static String decode(ByteBuffer bytes) throws FrameException {
		CharsetDecoder decode = Charset.forName("UTF8").newDecoder();
		decode.onMalformedInput(codingErrorAction);
		decode.onUnmappableCharacter(codingErrorAction);
		// decode.replaceWith( "X" );
		String s;
		try {
			bytes.mark();
			s = decode.decode(bytes).toString();
			bytes.reset();
		} catch (CharacterCodingException e) {
			throw new FrameException(CloseFrame.NO_UTF8, e);
		}
		return s;
	}

	// **************************************************************************
	// ** isValid
	// **************************************************************************
	/**
	 * Returns true if the ByteBuffer contains a valid UTF-8 encoded string.
	 */
	public static boolean isValid(ByteBuffer data) {
		return isValidUTF8(data, 0);
	}

	// **************************************************************************
	// ** isValidUTF8
	// **************************************************************************
	/**
	 * Returns true if the ByteBuffer contains a valid UTF-8 encoded string.
	 * Based on the "Flexible and Economical UTF-8 Decoder" algorithm by Bjoern
	 * Hoehrmann (http://bjoern.hoehrmann.de/utf-8/decoder/dfa/)
	 *
	 * @param data
	 *            ByteBuffer
	 * @param off
	 *            Offset (for performance reasons)
	 */
	private static boolean isValidUTF8(ByteBuffer data, int off) {
		int len = data.remaining();
		if (len < off) {
			return false;
		}
		int state = 0;
		for (int i = off; i < len; ++i) {
			state = utf8d[256 + (state << 4) + utf8d[(0xff & data.get(i))]];
			if (state == 1) {
				return false;
			}
		}
		return true;
	}

	private static final int[] utf8d = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	        0, 0, 0, 0, 0, 0, // 00..1f
	        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 20..3f
	        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 40..5f
	        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 60..7f
	        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, // 80..9f
	        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, // a0..bf
	        8, 8, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, // c0..df
	        0xa, 0x3, 0x3, 0x3, 0x3, 0x3, 0x3, 0x3, 0x3, 0x3, 0x3, 0x3, 0x3, 0x4, 0x3, 0x3, // e0..ef
	        0xb, 0x6, 0x6, 0x6, 0x5, 0x8, 0x8, 0x8, 0x8, 0x8, 0x8, 0x8, 0x8, 0x8, 0x8, 0x8, // f0..ff
	        0x0, 0x1, 0x2, 0x3, 0x5, 0x8, 0x7, 0x1, 0x1, 0x1, 0x4, 0x6, 0x1, 0x1, 0x1, 0x1, // s0..s0
	        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, // s1..s2
	        1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, // s3..s4
	        1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, // s5..s6
	        1, 3, 1, 1, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 // s7..s8
	};
}