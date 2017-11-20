package Model;

import Utility.FileManager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ImageManager {

  /* A map with file as key and its corresponding image as value. */
  private static Map<File, Image> images;
  /* A Logger. */
  private static final Logger logger = Logger.getLogger(Image.class.getName());
  /* A ConsoleHandler. */
  private static final Handler consoleHandler = new ConsoleHandler();
  private static final String path = "./im  ages.ser";

  /**
   * An ImageManager with filePath.
   *
   * @throws ClassNotFoundException if the class path is not updated
   * @throws IOException if stream to file with filePath cannot be written or closed
   */
  public ImageManager() throws ClassNotFoundException, IOException {
    //Code adapted from Paul's slides
    //http://www.teach.cs.toronto.edu/~csc207h/fall/lectures.shtml
    images = new HashMap<>();
    File file = new File(path);
    logger.setLevel(Level.ALL);
    consoleHandler.setLevel(Level.ALL);
    logger.addHandler(consoleHandler);
    if (file.exists()) {
      readFromFile();
    } else {
      if (file.createNewFile()) {
        logger.log(Level.FINE, path);
      }
    }
  }

  /**
   * Read from the file with path.
   *
   * @throws ClassNotFoundException if the class path is not updated
   */
  private static void readFromFile() throws ClassNotFoundException {
    //Code adapted from Paul's slides
    //http://www.teach.cs.toronto.edu/~csc207h/fall/lectures.shtml
    FileManager fm = new FileManager();
    try {
      ObjectInput input = fm.readFromFile(path);

      // deserialize the Map
      images = (HashMap<File, Image>) input.readObject();
      input.close();
    } catch (IOException ex) {
      logger.log(Level.WARNING, "Cannot read from images.ser");
    }
  }

  /**
   * Add an image record to images.
   *
   * @param record the Image that is about to be added
   */
  public static void add(Image record) {
    images.put(record.getFile(), record);
  }

  /**
   * Save the changes to the file with filePath.
   *
   * @throws IOException if stream to file with filePath cannot be written or closed
   */
  private static void saveToFile() throws IOException {
    //Code adapted from Paul's slides
    //http://www.teach.cs.toronto.edu/~csc207h/fall/lectures.shtml
    FileManager fm = new FileManager();
    ObjectOutput output = fm.saveToFile(path);

    // serialize the Map
    output.writeObject(images);
    output.close();
  }

  /**
   * Return the String representation of this ImageManager.
   *
   * @return the String representation of this ImageManager
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (Image i : images.values()) {
      result.append(i.toString());
      result.append(System.lineSeparator());
    }
    return result.toString();
  }


  /**
   * Check if this file is an existing key in images. If not, add this file and its corresponding
   * Image to images. Save this ImageManager to a serialized file and return its corresponding image
   * object.
   *
   * @param file the file key to check in images.
   * @return the image corresponding to this file.
   * @throws IOException if saving serialized file fails.
   */
  public static Image checkKey(File file) throws IOException {
    if (!images.containsKey(file)) {
      add(new Image(file));
      saveToFile();
    }
    return images.get(file);
  }

  /**
   * Remove the old key f1 and add a new entry to images with Image i.
   *
   * @param f1 the old key to remove
   * @param i the Image to add
   * @throws IOException if saving serialized file fails.
   */
  private static void updateKey(File f1, Image i) throws IOException {
    images.remove(f1);
    add(i);
    saveToFile();
  }

  /**
   * Rename this image with string path filePath to newName and save serialized file.
   *
   * @param filePath the path of this image file.
   * @param newName the name to change to.
   * @return the updated Image.
   * @throws IOException if saving serialized file fails.
   */
  public static Image renameImage(String filePath, String newName) throws IOException {
    File file = new File(filePath);
    Image i = checkKey(file);

    i.setName(newName);

    updateKey(file, i);

    return i;
  }

  /**
   * Add tag to the image file denoted by filePath and save serialized file.
   *
   * @param filePath the path of this image file.
   * @param tag the tag to be added.
   * @return the updated Image.
   * @throws IOException if saving serialized file fails.
   */
  public static Image addTag(String filePath, String tag) throws IOException {
    File file = new File(filePath);
    Image i = checkKey(file);

    i.addTag(tag);

    updateKey(file, i);

    return i;
  }

  /**
   * Delete tag from the image file denoted by filePath and save serialized file.
   *
   * @param filePath the path of this image file.
   * @param tag the tag to be deleted
   * @return the updated Image
   * @throws IOException if saving serialized file fails.
   */
  public static Image deleteTag(String filePath, String tag) throws IOException {
    File file = new File(filePath);
    Image i = checkKey(file);

    i.deleteTag(tag);

    updateKey(file, i);

    return i;
  }


  /**
   * Move this image denoted by oldPath to newPath and save serialized file.
   *
   * @param oldPath the path of this image file
   * @param newPath the new path to move this image file to
   * @throws IOException if saving serialized file fails.
   */
  public static void move(String oldPath, String newPath) throws IOException {
    File file = new File(oldPath);
    Image i = checkKey(file);

    i.move(newPath);

    updateKey(file, i);
  }

  /**
   * Return a String of the log of the image file denoted by filePath.
   *
   * @param filePath the image file to check the log of.
   * @return the String log.
   * @throws IOException if saving serialized file fails.
   */
  public static String getLog(String filePath) throws IOException {
    File file = new File(filePath);
    return checkKey(file).getLog();
  }

  /**
   * Return an ArrayList of tag(s) of the image file denoted by filePath.
   *
   * @param filepath the image file to check the log of.
   * @return An ArrayList of the tag(s).
   * @throws IOException if saving serialized file fails.
   */
  public static ArrayList<String> getTags(String filepath) throws IOException {
    File file = new File(filepath);
    return checkKey(file).getCurrentTags();
  }


  /**
   * Return an ArrayList of all past names of the image file denoted by filePath.
   *
   * @param filepath the image file to check the past names of.
   * @return an ArrayList of all past names
   */
  public static ArrayList<String> getPastName(String filepath) {
    File file = new File(filepath);
    try {
      return checkKey(file).getPastNames();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}

