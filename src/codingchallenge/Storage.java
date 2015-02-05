package codingchallenge;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//Class to serialize and deserialize objects
public class Storage {

	public static void Serialize(String filename, Object obj) {
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(filename);
			ObjectOutputStream out = null;
			out = new ObjectOutputStream(fileOut);
			out.writeObject(obj);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object Deserialize(String filename) {
		Object obj = null;
		File f = new File(filename);
		try {
			if (!f.exists()) {
				f.createNewFile();
				return null;
			}
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			obj = in.readObject();
			in.close();
			fileIn.close();
		} catch (EOFException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return obj;
	}
}
