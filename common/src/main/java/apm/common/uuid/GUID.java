package apm.common.uuid;

/**
 * GUID stands for Globally Unique IDentifier. The GUID class creates a two long
 * based upon two pieces of information:
 * <ol>
 * <li>The current system time
 * <li>The local machine's IP address.
 * </ol>
 * 
 * This combination creates a number that should be unique in both space and
 * time.
 * 
 * @version 2.0
 * @author Duffy Gillman,Conan Albrecht
 */
public class GUID {

	private static final java.text.SimpleDateFormat GUID_SDF = new java.text.SimpleDateFormat(
			"yyyyMMddHHmmssSSS");
	private static String lastGUID = "";
	private static String lastShortGUID = "";
	private static final byte HEX[] = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static final byte LETTERS[] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'k', 'l', 'm', 'n', 'o', 'p' };
	
	public static void main(String[] args){
		System.out.println(getGUID());
	}

	/**
	 * Generates a new globally unique id, based upon current system time and
	 * system IP address
	 */
	public static synchronized String getGUID() {
		String newGUID = "";

		do {
			long now = Long.parseLong(GUID_SDF.format(new java.util.Date()));
			newGUID = Long.toHexString(now);

			try {
				byte ip[] = java.net.InetAddress.getLocalHost().getAddress();

				for (int i = 0; i < ip.length; i++) {
					int n = ip[i];

					if (ip[i] < 0) {
						n += 256;
					}

					newGUID += Integer.toHexString(n);
				}
			} catch (Exception e) {
				newGUID += "00000000";
			}

		} while (newGUID.equals(lastGUID));

		lastGUID = newGUID;
		return lastGUID;
	}

	/**
	 * Generates a new globally unique id, based upon current system time and
	 * system IP address
	 */
	public static synchronized String getGUIDLetters() {
		return convertToRoman(getGUID());
	}

	/** Generates a GUID with time only (makes a shorter guid but less unique) */
	public static synchronized String getShortGUID() {
		String newShortGUID = "";

		do {
			long now = Long.parseLong(GUID_SDF.format(new java.util.Date()));
			newShortGUID = Long.toHexString(now);

		} while (newShortGUID.equals(lastShortGUID));

		lastShortGUID = newShortGUID;
		return lastShortGUID;
	}

	/** Generates a GUID with time only (makes a short guid but less unique) */
	public static synchronized String getShortGUIDLetters() {
		return convertToRoman(getShortGUID());
	}

	/** Helper method that converts the hex characters to regular roman letters */
	private static String convertToRoman(String hex) {
		byte guid[] = hex.getBytes();
		byte letters[] = new byte[guid.length];
		for (int i = 0; i < guid.length; i++) {
			for (int j = 0; j < HEX.length; j++) {
				if (guid[i] == HEX[j]) {
					letters[i] = LETTERS[j];
					break;
				}
			}
		}
		return new String(letters);
	}

}