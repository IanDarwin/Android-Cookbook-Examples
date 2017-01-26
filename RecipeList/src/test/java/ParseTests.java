import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import static org.junit.Assert.*;

import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

import nl.codestone.recipelist.Datum;
import nl.codestone.recipelist.MainActivity;

public class ParseTests {

	String testData = "<recipes>" +
		"<recipe><id>123</id><title>New Beginnings</title></recipe>" +
		"<recipe><id>456</id><title>Old Endings</title><other-cruft>more</other-cruft></recipe>" +
		"</recipes>";

	@Test
	public void testParse1() throws Exception {
		List<Datum> actual = MainActivity.parse(new ByteArrayInputStream(testData.getBytes()));
		assertEquals(2, actual.size());
		Datum d = actual.get(0);
		assertEquals(123, d.getId());
		assertEquals("New Beginnings", d.getTitle());
	}

	@Test
	public void testParse2() throws Exception {
		List<Datum> actual = MainActivity.parse2(new ByteArrayInputStream(testData.getBytes()));
		assertEquals(2, actual.size());
		Datum d = actual.get(0);
		assertEquals(123, d.getId());
		assertEquals("New Beginnings", d.getTitle());
	}
}
